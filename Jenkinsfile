pipeline {
  agent any

  environment {
    // Used by ConfigLoader
    ENV = "jenkins"
    HEADLESS = "true"
    BASE_URL = "https://opensource-demo.orangehrmlive.com/"
    // For real projects, move credentials to Jenkins Credentials and use withCredentials{}
    USERNAME = "Admin"
    PASSWORD = "admin123"
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build & Test') {
      steps {
        sh 'mvn -B clean test'
      }
    }

    stage('Archive Screenshots') {
      when {
        expression { fileExists('screenshots') }
      }
      steps {
        archiveArtifacts artifacts: 'screenshots/**/*.png', fingerprint: true
      }
    }
  }

  post {
    always {
      junit 'target/surefire-reports/*.xml'
    }
  }
}
