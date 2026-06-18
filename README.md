## Docker Network Setup 

Manually Create Docker Network for Communication Between Containers (One-Time Setup)
Run the following command in the terminal to create a Docker network:

command - docker network create wildace-network
Update the configuration file to ensure the network is mentioned in each docker compose file :

networks:
  wildace-network:
    external: true
    name: wildace-network
