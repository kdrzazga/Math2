pipeline {
    agent any
    
    tools{
        git 'Default'   
    }
    
    triggers {
        cron('15 0 * * *')   
    }
    
    stages {
        stage('checkout'){
            steps {
                git 'http://github.com/kdrzazga/Math2'
            }
            post {
                always {
                    echo 'Checkout finished.'
                }
                success {
                    echo "Project checked out from GH"
                }
            }
        }
        stage('build') {
            steps {
                sh "mvn -Dmaven.test.failure.ignore=true clean compile"
            }
            post {
                failure {
                    echo "Build failed"
                }
            }            
        }
        stage('test'){
            steps {
                sh "mvn test"
            }
            post {
                success {
                    echo "Tests passed"
                }
            }
        }
        stage('build'){
            steps {
                script {
                    sh "mvn package"
                }
            }
        }
        stage('deploy') {
            steps {
                script {
                    def image = docker.build("Math2:${env.BUILD_ID}", ".")
                    image.push()
                    sh "docker run -d -p 8080:8080 Math2:${env.BUILD_ID}"
                }
            }
        }
    }
}
