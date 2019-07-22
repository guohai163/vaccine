pipeline {
  agent any
  environment {
    TAG_SERVER = 'guohai@guohai.org'
    TAG_PATH = '/data/vaccine.guohai.org'

  }

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
            sh "scp -i ${JENKINS_HOME}/id_rsa ${WORKSPACE}/target/vaccine-0.0.1-SNAPSHOT.jar guohai@guohai.org:${TAG_PATH}"
            sh "ssh -i ${JENKINS_HOME}/id_rsa guohai@guohai.org md5sum ${TAG_PATH}/vaccine-0.0.1-SNAPSHOT.jar"
            input "请确认本地MD5与服务器上的MD5一致再继续执行?"
            sh "ssh -i ${JENKINS_HOME}/id_rsa guohai@guohai.org ${TAG_PATH}/spring-boot.sh restart ${TAG_PATH}/vaccine-0.0.1-SNAPSHOT.jar"
        }
    }
  }
}