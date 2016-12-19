/*
 * Copyright 2014 Transmode AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.transmode.gradle.plugins.docker.client

import com.google.common.base.Preconditions
import org.gradle.api.GradleException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NativeDockerClient implements DockerClient {

    private static final Logger LOG = LoggerFactory.getLogger('NativeDockerClient')

    private final String binary;

    NativeDockerClient(String binary) {
        Preconditions.checkArgument(binary as Boolean, "Docker binary can not be empty or null.")
        this.binary = binary
    }

    @Override
    void buildImage(File buildDir, String tag) {
        Preconditions.checkArgument(tag as Boolean, "Image tag can not be empty or null.")
        executeAndWait(binary, 'build', '--no-cache=true', '--force-rm=true', '-t', tag, buildDir.toString())
    }

    @Override
    void pushImage(String tag) {
        Preconditions.checkArgument(tag as Boolean, "Image tag can not be empty or null.")
        executeAndWait(binary, 'push', tag)
    }

    @Override
    void tagImage(String imageIdentifier, String newTag) {
        Preconditions.checkArgument(imageIdentifier as Boolean, "Image identifier can not be empty or null.")
        Preconditions.checkArgument(newTag as Boolean, "New image tag can not be empty or null.")
        executeAndWait(binary, 'tag', imageIdentifier, newTag)
    }

    private static void executeAndWait(String... commands) {
        def process = commands.execute()
        process.consumeProcessOutput(
                new LoggingAppendable(LOG, "info"),
                new LoggingAppendable(LOG, "error")
        )
        process.waitFor()
        if (process.exitValue()) {
            throw new GradleException(
                    "Docker execution failed\nCommand line [${commands}] returned:\n${process.err.text}")
        }
    }
}
