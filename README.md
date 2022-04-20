# Facepoke

Facepoke provides a fresh perspective at the world of Pokemon, in the form of a REST api that returns pokemon information.
The API has two main endpoints:
- ##### Basic pokemon information 
```/HTTP/GET /pokemon/<pokemon name> ```

Given a pokemon name, returns standard Pokemon description and additional information

- #### Translated pokemon description 
``` /HTTP/GET /pokemon/translated/<pokemon name> ```

Given a pokemon name, returns translated Pokemon description and other basic information

## How to run it

- Pull the docker image:
``` docker pull nolanofra/facepoke:latest ```

or alternatively build the image from the Dockerfile within the project

- Run it
``` docker run -d -p 5000:5000 nolanofra/facepoke:latest ```

To play with the API you can call it using [httpie](https://httpie.io/)

``` http localhost:5000/health ```

``` http localhost:5000/pokemon/mewtwo ```

``` http localhost:5000/pokemon/translated/mewtwo ```

## Integration tests
To run the integration tests you need to install [sbt](https://www.scala-sbt.org/1.x/docs/Setup.html) 

Once sbt has been installed, you can run `sbt integrationTests`
This task will produce the application jar and build a docker image with it.

Integration tests use [test container library](https://github.com/testcontainers/testcontainers-scala).
Two different containers interact with each other during the tests: the `Api Server` (hosting the Facepoke API) and the `MockServer` container, to mock the external dependencies.

### Future improvements
There might be several improvements to do for making Facepoke a production-ready API:

- Improve the observability of the API, providing a structured logging
- Add a CI/CD flow, for instance using GitHub Action
- A cache can be introduced (local or distributed) to improve availability and reduce latency
