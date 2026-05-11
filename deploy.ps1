Write-Host "Building backend JAR..."
Push-Location survey-platform
./mvnw package -DskipTests -q
if (-not $?) {
    Write-Error "Maven build failed. Aborting."
    Pop-Location
    exit 1
}
Pop-Location

Write-Host "Starting Docker Compose..."
docker compose up --build @args
