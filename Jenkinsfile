pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'Java17'
    }

    environment {
        SONARQUBE_SERVER = 'SonarQubeServer'
        SONARQUBE_TOKEN  = credentials('sonarqube-token')
        TELEGRAM_CHAT_ID = '1868802578'
        TELEGRAM_TOKEN   = credentials('7964045222:AAElE5m35X1rUfU-2lO0ZpLzwuy_esmMsvY')
        DOCKER_IMAGE     = "jenkins/jenkins"
        OPENSHIFT_PROJECT = "kautsarhaqi-dev"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/your-repo/handson-terlengkap.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Unit Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${SONARQUBE_SERVER}") {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=handson-demo -Dsonar.host.url=http://sonarqube:9000 -Dsonar.login=$SONARQUBE_TOKEN'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build & Push Docker') {
            steps {
                sh """
                docker build -t $DOCKER_IMAGE .
                docker push $DOCKER_IMAGE
                """
            }
        }

        stage('Deploy to OpenShift') {
            steps {
                sh """
                oc project $OPENSHIFT_PROJECT
                oc apply -f openshift/deployment.yaml
                oc rollout restart deployment handson-app
                """
            }
        }
    }

    post {
        success {
            script {
                sh """
                curl -s -X POST https://api.telegram.org/bot${TELEGRAM_TOKEN}/sendMessage \
                -d chat_id=${TELEGRAM_CHAT_ID} \
                -d parse_mode=Markdown \
                -d text='✅ Pipeline sukses! Aplikasi sudah terdeploy di OpenShift.'
                """
            }
        }
        failure {
            script {
                sh """
                curl -s -X POST https://api.telegram.org/bot${TELEGRAM_TOKEN}/sendMessage \
                -d chat_id=${TELEGRAM_CHAT_ID} \
                -d parse_mode=Markdown \
                -d text='❌ Pipeline gagal! Cek Jenkins dan SonarQube.'
                """
            }
        }
    }
}
