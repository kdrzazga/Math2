pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo off
                echo 'Stage: checkout'
                if exist WA2 rmdir /q /s WA2
                mkdir WA2
                cd WA2
                git clone https://github.com/kdrzazga/Math2.git
                echo ---------------------------------
            }
        }
        stage('Build and Test') {
            steps {
                echo off
                echo 'Stage: Build and Test'
                cd WA2\Math2\
                dir
                c:\maven\bin\mvn clean test
                echo ---------------------------------
            }
        }
    }
}