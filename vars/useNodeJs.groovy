def call(String nodeVersion, String npmVersion = '') {
    def platform = 'linux-x64'
    if (runCommand('uname').contains('Darwin')) {
        platform = 'darwin-x64'    
    }

    def nodeJsToolDir = "${env.JENKINS_HOME}/../tools/nodejs"
    def binPath = "${nodeJsToolDir}/node-v${nodeVersion}-${platform}/bin"
    env.PATH = "${binPath}:${env.PATH}"

    if (!fileExists(nodeJsToolDir)) {
        sh "mkdir -p ${nodeJsToolDir}"
    }

    if (!fileExists(binPath)) {
        sh "cd ${nodeJsToolDir}; curl -LO \"https://nodejs.org/dist/v${nodeVersion}/node-v${npmVersion}-${platform}.tar.gz\""
        sh "cd ${nodeJsToolDir}; tar xvf \"node-v${nodeVersion}-${platform}.tar.gz\""
        sh "cd ${nodeJsToolDir}; rm \"node-v${nodeVersion}-${platform}.tar.gz\""
        sh "PATH=\"${binPath}:\$PATH\" npm i -g \"npm@${npmVersion}\";"
    }
}