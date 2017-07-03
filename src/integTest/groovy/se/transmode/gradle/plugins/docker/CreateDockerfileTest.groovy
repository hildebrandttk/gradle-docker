/**
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
package se.transmode.gradle.plugins.docker

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import java.time.Duration

import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.is
import static org.junit.Assert.assertThat

class CreateDockerfileTest {
  @Test
  public void compareDockFile() {
    Project project = ProjectBuilder.builder().build()
    project.apply plugin: 'docker'

    def task = project.task('docker', type: DockerTask)

    // don't actually execute docker, just build Dockerfile
    task.dockerBinary = "/bin/true"

    // example pulled from "http://docs.docker.io/en/latest/use/builder/#dockerfile-examples"
    task.baseImage "ubuntu"
    task.maintainer 'Guillaume J. Charmes "guillaume@dotcloud.com"'

    task.runCommand 'echo "deb http://archive.ubuntu.com/ubuntu precise main universe" > /etc/apt/sources.list'
    task.runCommand "apt-get update"

    task.runCommand "apt-get install -y inotify-tools nginx apache2 openssh-server"
    task.addHealthCheck "/healthCheck.sh"
    task.addHealthCheck(Duration.ofSeconds(1), Duration.ofSeconds(5), "curl -f http://localhost/ || exit 1")
    task.addHealthCheck(Duration.ofSeconds(1), Duration.ofMinutes(5), Duration.ofHours(1), 5,
            "/bin/false")

    def expectedDockerFile = this.getClass().getResource("nginx.Dockerfile").text.trim()
    def actualDockerFile = task.buildDockerfile().instructions.join(System.getProperty('line.separator'))

    assertThat actualDockerFile, is(equalTo(expectedDockerFile))
  }
}

