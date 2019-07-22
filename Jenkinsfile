pipeline {
  agent any
  environment {
    TAG_SERVER = 'guohai@guohai.org'
    TAG_PATH = '/data/vaccine.guohai.org'
    PACKAGE_NAME = ${projectName}

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

    stage ('deploy') {
        steps {
            sh "md5sum ${WORKSPACE}/target/*.jar"
            withCredentials([sshUserPrivateKey(credentialsId: 'guohai.org', keyFileVariable: 'guohai_org_key', passphraseVariable: '', usernameVariable: '')]) {
                sh "scp -i ${guohai_org_key} ${WORKSPACE}/target/*.jar ${TAG_SERVER}:${TAG_PATH}/${PACKAGE_NAME}.jar"
                sh "ssh -i ${guohai_org_key} ${TAG_SERVER} md5sum ${TAG_PATH}/${PACKAGE_NAME}.jar"
                sh "ssh -i ${guohai_org_key} ${TAG_SERVER} ${TAG_PATH}/spring-boot.sh restart ${TAG_PATH}/${PACKAGE_NAME}.jar"
            }

        }
    }
  }
}