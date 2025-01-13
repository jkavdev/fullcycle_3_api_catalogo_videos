<center>
  <p align="center">
    <img src="https://user-images.githubusercontent.com/20674439/158480514-a529b310-bc19-46a5-ac95-fddcfa4776ee.png" width="150"/>&nbsp;
    <img src="https://icon-library.com/images/java-icon-png/java-icon-png-15.jpg"  width="150" />
  </p>  
  <h1 align="center">🚀 Microserviço: Catálogo de Vídeos com Java</h1>
  <p align="center">
    Microserviço referente ao backend do Catálogo de Vídeos<br />
    Utilizando Clean Architecture, TDD e as boas práticas atuais de mercado
  </p>
</center>
<br />

## Ferramentas necessárias

- JDK 17
- IDE de sua preferência
- Docker

## Rodando no windows 11
tem que habilitar o SUDO no windows para que o run.sh funcione

sera dara erro de permissao

````shell
$ ./run.sh
Error response from daemon: network with name elastic already exists
es01
Sudo is disabled on this machine. To enable it, go to the Developer Settings page in the Settings app
mkdir: cannot change permissions of '.docker': Permission denied
mkdir: cannot change permissions of '.docker/es01': Permission denied
validating C:\Users\jkavdev\Repo\github\jkavdev\fullcycle_3_api_catalogo_videos\sandbox\elk\docker-compose.yml: volumes.es01 Additional property elastic is not allowed

````