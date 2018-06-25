## Mancala Game

### How to Setup
1. Download [Docker For Mac](https://docs.docker.com/docker-for-mac/) on your machine.
2. Install the Docker Application and make sure you have the latest version installed. Make sure you have the Docker version up from `17.12.0+`.
4. For others OS look at [Docker Install](https://docs.docker.com/compose/install/).

### How to Run
1. Open the terminal
2. Clone the project: ```git clone https://github.com/felipehaack/mancala-game.git```
3. And go to the application folder ```cd mancala-game```

### Run backend
1. Go to the folder: `cd game-mancala-api`
2. Run the backend container by: `docker-compose up -d`
3. The default URL to access the backend is: `localhost:9001`

### Run frontend
1. Go to the folder: `cd game-mancala-website`
2. Run the backend container by: `docker-compose up -d`
3. Once the docker application has been launched successfully, go to [localhost:9000](http://localhost:9000).

### Improvements
1. Add tests for both application
2. Make the user interface friendly
3. Handle better the material classes
4. Support more than 2 players