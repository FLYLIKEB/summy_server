#!/bin/bash
set -euo pipefail

echo "===== macOS용 Docker 설치 시작 ====="

# Homebrew가 설치되어 있는지 확인
if ! command -v brew &> /dev/null; then
    echo "Homebrew가 설치되어 있지 않습니다. 설치를 시작합니다..."
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    
    # Homebrew PATH 설정 (Intel Mac)
    if [[ $(uname -m) == "x86_64" ]]; then
        echo 'eval "$(/usr/local/bin/brew shellenv)"' >> ~/.zprofile
        eval "$(/usr/local/bin/brew shellenv)"
    # Apple Silicon Mac
    else
        echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zprofile
        eval "$(/opt/homebrew/bin/brew shellenv)"
    fi
    
    echo "Homebrew 설치 완료"
else
    echo "Homebrew가 이미 설치되어 있습니다."
fi

# Docker Desktop 설치
echo "Docker Desktop 설치 중..."
brew install --cask docker

# Docker CLI 설치 (Docker Desktop 없이 CLI만 필요한 경우)
# brew install docker docker-compose

echo "Docker 설치가 완료되었습니다."
echo "Docker Desktop을 실행해주세요. Finder > Applications > Docker 에서 실행할 수 있습니다."
echo "또는 다음 명령어로 실행할 수 있습니다:"
echo "open /Applications/Docker.app"

# Docker Desktop 실행
read -p "Docker Desktop을 지금 실행하시겠습니까? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    open /Applications/Docker.app
    echo "Docker Desktop을 실행했습니다. 초기화가 완료될 때까지 기다려주세요."
    echo "초기화가 완료되면 Docker 아이콘이 메뉴바에 나타납니다."
fi

echo "===== Docker 설치 프로세스 완료 =====" 