def call(String nodeVersion = '', String npmVersion = '') {
    if (!nodeVersion) {
        nodeVersion = runCommand("python -c \"import json; data = json.load(open('package.json')); print(data['engines']['node'])\"");
    }
    if (!npmVersion) {
        npmVersion = runCommand("python -c \"import json; data = json.load(open('package.json')); print(data['engines']['npm'])\"");
    }

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
        sh "cd ${nodeJsToolDir}; curl -LO \"https://nodejs.org/dist/v${nodeVersion}/node-v${nodeVersion}-${platform}.tar.gz\""
        sh "cd ${nodeJsToolDir}; tar xvf \"node-v${nodeVersion}-${platform}.tar.gz\""
        sh "cd ${nodeJsToolDir}; rm \"node-v${nodeVersion}-${platform}.tar.gz\""
        sh "PATH=\"${binPath}:\$PATH\" npm i -g \"npm@${npmVersion}\";"
    }
}