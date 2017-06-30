# gradle-dcompose-plugin

When working with Gradle projects, there has always been a "technology break" when using Docker. First you 
would need to run a build like ```gradle assemble``` and afterwards ```docker-compose up``` in order to test 
your code locally.

This plugin aims at fully integrating the Docker container management into the Gradle build itself thus 
eleminating the need for docker-compose. It uses Gradle's UP-TO-DATE checks in order to determine whether a 
container should be recreated.


# Prerequisites

This plugin requires: 
* Gradle >= 2.8 (older versions possibly working, but not tested)
* Docker (host) >= 1.10.0
* **Please note:** Docker for Mac (native) currently doesn't support redirecting standard streams to/from a container.

# Documentation
For the full documentation head over to the [wiki](https://github.com/chrisgahlert/gradle-dcompose-plugin/wiki)!

# Example

An example project says more than a thousand words:
https://github.com/chrisgahlert/gradle-dcompose-sample

# Advanced example 

#### src/main/docker/Dockerfile

```dockerfile
FROM 'nginx:latest'
COPY index.html /www/
```

#### build.gradle
```gradle
dcompose {
  networks {
    frontend
    backend
  }
  
  dbData {
    image = 'mongo:latest'
    volumes = ['/var/lib/mongodb']
    preserveVolumes = true
  }
  db {
    image = 'mongo:latest'
    volumesFrom = [dbData]
    networks = [backend]
    aliases = ['mongo_db']
  }
  cache {
    image = 'redis:latest'
    networks = [backend]
  }
  web {
    baseDir = file("$buildDir/docker/")
    env = ['MONGO_HOST=mongo_db', 'REDIS_HOST=cache']
    repository = 'someuser/mywebimage:latest'
    networks = [frontend, backend]
  }
}

task copyDockerData(type: Sync) {
  from 'src/main/www/' // Contains index.html
  from 'src/main/docker/'
  into dcompose.web.baseDir
}
buildWebImage.dependsOn copyDockerData
```

#### Running

Launch the build by running `gradle startWebContainer`. This should automatically 
pull/create/start all required containers. Whenever something changes, you just need 
to re-run this build and everything, that needs to be recreated will be.
