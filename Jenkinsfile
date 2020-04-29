pipeline {
  agent any
  environment {
//     TAG_SERVER = 'guohai@guohai.org'
//     TAG_PATH = '/data/vaccine.guohai.org'
    //目标服务器启动停止springboot脚本路径
    TAG_SCRIPT = '/data/spring-boot.sh'
  }

  stages {
    stage ('build') {
      steps {
         script{
             def mvnHome = tool 'maven 3.6.0'
             sh "${mvnHome}/bin/mvn clean package"
             echo "build over"
         }

      }
    }
    stage ('Archive') {
      steps {
        archiveArtifacts artifacts: '**/target/*.jar',fingerprint: true
      }
    }
    stage ('deploy') {
        steps {
            sh "md5sum ${WORKSPACE}/target/*.jar"
            withCredentials([sshUserPrivateKey(credentialsId: "${SERVER_ID}", keyFileVariable: "${SERVER_KEY}", passphraseVariable: '', usernameVariable: '')]) {
                sh "scp -i ${guohai_org_key} ${WORKSPACE}/target/*.jar ${TAG_SERVER}:${TAG_PATH}/${JOB_BASE_NAME}.jar"
                sh "ssh -i ${guohai_org_key} ${TAG_SERVER} md5sum ${TAG_PATH}/${JOB_BASE_NAME}.jar"
                sh "ssh -i ${guohai_org_key} ${TAG_SERVER} ${TAG_SCRIPT} restart ${TAG_PATH}/${JOB_BASE_NAME}.jar --spring.config.location=${TAG_PATH}/application.yml"
            }

        }
    }
  }
}
