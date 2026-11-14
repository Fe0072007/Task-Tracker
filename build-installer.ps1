
$ErrorActionPreference = "Stop"

# Configuracoes
$appName = "Task Tracker CLI"
$version = "1.1.1"
$jpackage = "C:\Program Files\Java\jdk-27\bin\jpackage.exe"
$jar = "task-cli-$version.jar"

Set-Location $PSScriptRoot

# Verificar ferramentas
if (-not (Test-Path $jpackage)) {
    throw "jpackage nao encontrado: $jpackage"
}

# Compilar projeto
Write-Host "Compilando projeto..."

& mvn clean package

if ($LASTEXITCODE -ne 0) {
    throw "Falha na compilacao Maven."
}

# Preparar entrada
Write-Host "Preparando arquivos..."

if (-not (Test-Path "target\$jar")) {
    throw "JAR nao encontrado: target\$jar"
}

if (Test-Path "dist-input") {
    Remove-Item "dist-input" -Recurse -Force
}

New-Item -ItemType Directory "dist-input" | Out-Null

Copy-Item "target\$jar" "dist-input\"
Copy-Item "installer-resources\task-cli.cmd" "dist-input\"

# Criar pasta de saida
New-Item -ItemType Directory -Force "dist-installer" | Out-Null

# Argumentos do jpackage
$jpackageArgs = @(
    "--type", "exe",
    "--name", $appName,
    "--app-version", $version,
    "--input", "dist-input",
    "--main-jar", $jar,
    "--main-class", "com.tasktracker.Main",
    "--win-console",
    "--win-dir-chooser",
    "--resource-dir", "installer-resources",
    "--dest", "dist-installer"
)

Write-Host "Gerando instalador Windows..."

& $jpackage @jpackageArgs

if ($LASTEXITCODE -ne 0) {
    throw "Falha na geracao do instalador."
}

Write-Host "Instalador gerado com sucesso!"
