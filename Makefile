.PHONY: up
up:
	docker compose up -d

.PHONY: down
down:
	docker compose down

.PHONY: build
build: db.reset_owner
	docker compose build

.PHONY: server.logs
server.logs:
	docker compose logs -f maplestory

.PHONY: db.logs
db.logs:
	docker compose logs -f db

.PHONY: db.reset_owner
db.reset_owner:
	sudo chown -R "${shell id -u}:${shell id -g}" ./database/docker-db-data

.PHONY: db.backup
db.backup: db.reset_owner
	zip -r "backups/cosmic_db_$(shell date +%Y%m%d%H%M%S).zip" ./database/docker-db-data
