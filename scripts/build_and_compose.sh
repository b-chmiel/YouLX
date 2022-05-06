cd "${0%/*}"
cd ..
cd frontend
pnpm install
pnpm build
cd ..
cd backend
./gradlew bootBuildImage --imageName=youlx/backend 
cd ..
docker-compose build --no-cache
docker compose up --force-recreate