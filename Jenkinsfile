pipeline {
  agent any

  stages {
    stage ('build') {
      steps {
         script{
             def mvnHome = tool 'maven 3.6.0'
             println "test:"+mvnHome
             sh "${mvnHome}/bin/mvn clean package"
             echo "build over"
         }

      }
    }

    stage ('deploy') {
        steps {
            sh "md5sum ${WORKSPACE}/target/vaccine-0.0.1-SNAPSHOT.jar"
            sh "scp -i ${JENKINS_HOME}/id_rsa ${WORKSPACE}/target/vaccine-0.0.1-SNAPSHOT.jar guohai@guohai.org:/data/vaccine.guohai.org"
            sh "ssh -i ${JENKINS_HOME}/id_rsa guohai@guohai.org md5sum /data/vaccine.guohai.org/vaccine-0.0.1-SNAPSHOT.jar"
            sh "ssh -i ${JENKINS_HOME}/id_rsa guohai@guohai.org /data/vaccine.guohai.org/spring-boot.sh restart /data/vaccine.guohai.org/vaccine-0.0.1-SNAPSHOT.jar"
        }
    }
  }
}