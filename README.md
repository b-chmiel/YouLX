# YouLX

University assignment from `Projekt i implementacja systemów webowych`.

## Building

### Deployment

`docker-compose up`

Website will be available at: [http://localhost:8080](http://localhost:8080).

There are several test users available:
- login: user1, password: user1
- login: user2, password: user2
- login: user3, password: user3

### Development

Run backend:
```
cd backend
./gradlew bootRun
```

Run frontend:
```
cd frontend
pnpm i && pnpm run ng serve
```

Website will be available at: [http://localhost:4200](http://localhost:4200).


## Features

- Adding, modifying and deleting offers.
- Internal messaging system between Advertisers and Buyers.
- Draft mode for offers with private link.
- Filtering offers by tags with autocompletion.
- Searching offers by text query.
