if(-not(Get-Command mvn -ErrorAction SilentlyContinue)) {
    Write-Error "Maven (mvn) was not found on PATH. Install Maven and reopen the terinal."
    exit 1
}

$mavenVersion = & mvn -version 2>&1
if($LASTEXITCODE -ne 0) {
    Write-Error "Could not determine Java runtime Maven is using"
    exit 1
}

$versionText = $mavenVersion -join "`n"
$javaVersionMatch = [regex]::Match($VersionText, 'Java version:\s+(\d+)')

if(-not $javaVersionMatch.Success) {
    Write-Error "Could not parse the Java version Maven is using"
    exit 1
}
$javaMajorVersion = [int]$javaVersionMatch.Groups[1].Value
if($javaMajorVersion -lt 25) {
    Write-Error "Java version 25 or higher is required"
    exit 1
}

$execArgs = ($args | ForEach-Object { 
    if($_ -match '\s') {"'" + $_ + "'"} else { $_ } 
}) -join ' '
& mvn exec:java `
    "-Dexec.mainClass=com.finalProject.Main" `
    "-Dexec.args=$execArgs"