#  CONSTRUCCIÓN DE UNA APLICACIÓN DESPLEGADA EN HEROKU QUE HACE USO DE UNA ARQUITECTURA SIMPLE DE MICROSERVICIOS

### Página Web 🌐

[![Deployed to Heroku](https://www.herokucdn.com/deploy/button.png)](https://arep-spark-web-app.herokuapp.com/)

## REQUERIMIENTOS
Debe construir una aplicación para consultar el mercado de valores de las acciones negociadas en Bolsa. La aplicación recibirá el identificador de una acción, por ejemplo “MSFT” para Microsoft y deberá mostrar el histórico de la valoración intra-día, diaria, semanal y mensual. Se le pide que su implementación sea eficiente en cuanto a recursos así que debe implementar un Caché que permita evitar hacer consultas repetidas al API externo. Finalmente, se le pide que muestre como se puede enlazar con otro proveedor de servicios.

La arquitectura de la aplicación debe tener las siguientes características:
- El cliente Web debe ser un cliente asíncrono que use servicios REST desplegados en Heroku y use Json como formato para los mensajes.
- El servidor de Heroku servirá como un gateway para encapsular llamadas a otros servicios Web externos.
- La aplicación debe ser multiusuario.
- Todos los protocolos de comunicación serán sobre HTTP.
- Los formatos de los mensajes de intercambio serán siempre JSON.
- La interfaz gráfica del cliente debe ser los más limpia y agradable posible solo HTML y JS (Evite usar librerías complejas). Para invocar métodos REST desde el cliente usted puede utilizar la tecnología que desee.
- Debe construir un cliente Java que permita probar las funciones del servidor fachada. El cliente utiliza simples conexiones http para conectarse a los servicios. Este cliente debe hacer pruebas de concurrencia en su servidor de backend.
- La fachada de servicios tendrá un caché que permitirá que llamados que ya se han realizado a las implementaciones concretas con parámetros específicos no se realicen nuevamente.
- Se debe poder extender fácilmente, por ejemplo, es fácil agregar nuevas funcionalidades, o es fácil cambiar el proveedor de una funcionalidad.
- Debe utilizar maven para gestionar el ciclo de vida, git y github para almacenar al código fuente y heroku como plataforma de producción.

## CONSTRUCCIÓN Y ARQUITECTURA
El proyecto se realizó usando para el BACK-END el micro-framework para java _Spark_. El servicio se construyó de tal forma que soporta múltiples solicitudes de manera concurrente.\
En cuanto al FRONT-END, este se construyó utilizando la tecnología _ReactJS_. Como las fuentes del FRONT-END incluídas en este proyecto son las fuentes compiladas, no es posible apreciar el detalle de la construcción, por lo que el código fuente del FRONT-END fue almacenado en otro repositorio, de manera que si se desea ver la implementación y detalles del código, por favor refererirse a [FRONT-END Source Code](https://github.com/Brayandres/AREP-LAB-01-FRONT_END).

### Patrones Utilizados
- **Singleton**
	+ **Memoria Caché:** El servicio de memoria caché se implementó utilizando un _Singleton_, ya que este servicio representa un único punto de entrada para la solicitud de los datos, de manera que se maneja una única instancia del servicio. Dada la naturaleza del singleton, la clase se construyó Thread-Safe, por lo que la gestión de la instancia y el uso de los métodos es confiable.

- **Factory Method**
	+ **Múltiples Proveedores:** La parte de la integración de los proveedores se construyó usando _Factory-Method_. De esta forma se busca que la gestión y cambio de proveedores sea realmente sencillo de hacer. En el apartado de Extensibilidad se explica cómo se puede hacer. 

### Extensibilidad
Para poder extender fácilmente el servicio y añadir nuevos proveedores se debe, primero, tener (o crear) una clase que implemente la Interface ```ApiConnection```, que requiere que se implemente el método ```getStockValuationHistory()``` que devuelve una cadena con los datos de la consulta en formato JSON y recibe como parámetros el identificador de la acción (una cadena) y 2 ENUMs que representan el Periodo y el Intervalo, por lo que se "obliga" a que las implementaciones trabajen todas con los mismos valores de Periodo e Intervalo. Esta interface también tiene un método privado ```getIterablePropertyNameFromResponseJSON()``` que debe devolver un string con el nombre (exacto) de la clave del atributo (en el JSON) cuyo cuerpo contiene todas los datos con que se llena la tabla, ya que es sobre esta propiedad sobre la que se itera para poder construir la tabla en la página.

Finalmente, se debe implementar la interface ``` ApiConnectionCreator ``` y sobrescribir el método ``` createApiConnection() ```, que devuelve una instancia del servicio que se está agregando.

### Cliente de Pruebas Concurrentes
Para poder probar la característica de concurrencia de la aplicación, se construyó un servicio que crea una cierta cantidad de hilos y ejecuta por cada hilo una cantidad de solictudes, así, todas las solictudes se realizan de manera concurrente. Es importante destacar que es necesario que el servidor esté corriendo cuando se vaya a ejecutar el cliente de pruebas.\
Debido a la dificultad para hacer correr la aplicación durante la fase de pruebas, se dejaron las clases de prueba en otra rama, la rama ```concurrency-testing-client```, ya que si se dejaban en la rama ```main``` iban a fallar al realizar el despliegue en heroku.

En el apartado de **EJECUCIÓN DE PRUEBAS** se detalla cómo poder ejecutar las pruebas.

## PRERREQUISITOS
Para la realización y ejecución tanto del programa como de las pruebas, va a necesitar tener instaladas las siguientes herramientas:
* [Java](https://www.java.com/es/) - Java es un lenguaje de programación multipropósito, cuya plataforma de desarrollo es impulsada por Oracle. Es multiplataforma, lo que lo hace uno de los lenguajes más populares del mundo.
* [Maven](https://maven.apache.org/) - Herramienta que se encarga de estandarizar la estructura física de los proyectos de software, maneja dependencias (librerías) automáticamente desde repositorios y administra el flujo de vida de construcción de un software.
* [GIT](https://git-scm.com/) - Sistema de control de versiones que almacena cambios sobre un archivo o un conjunto de archivos, permite recuperar versiones previas de esos archivos y permite otras cosas como el manejo de ramas (branches).

Para asegurar que el usuario cumple con todos los prerrequisitos para poder ejecutar el programa, es necesario disponer de una consola o Símbolo del Sistema para ejecutar los siguientes comandos para comprobar que todos los programas están instalados correctamente.

```shell
java --version # java version "11.0.13"
mvn --version # Apache Maven 3.6.3
git --version # version 2.31.1
```

### Instalación del proyecto
Para descargar el proyecto de GitHub, primero debemos clonar este repositorio, ejecutando el siguiente comando en GIT.
```shell
git clone https://github.com/Brayandres/AREP-LAB-01.git
```
O si lo desea, puede descargarlo como archivo zip y luego descomprimirlo en su equipo.

### Compilación
Para compilar el proyecto utilizando la herramienta Maven, nos dirigimos al directorio donde se encuentra alojado el proyecto, y dentro de este ejecutamos en una consola o Símbolo del Sistema el siguiente comando:

```shell
mvn clean install -DskipTests
```

## EJECUCIÓN DE PRUEBAS
**Nota:** va a necesitar 2 ventanas de consola o símbolo del sistema que estén abiertas en el directorio principal del proyecto.
Para ejecutar las pruebas se deben ejecutar los siguientes comandos de manera ordenada:

(En la primera ventana)
```shell
# Moverse a la rama de pruebas
git branch -m "concurrency-testing-client"

# Actualizar la rama
git fetch origin concurrency-testing-client
git pull

# Compilar las fuentes
mvn clean install -DskipTests

# Correr el servidor de Back-End. Dejar el servidor corriendo
java -jar target/StockMarketConsultant-1.0-SNAPSHOT.jar
```

(En la segunda ventana)
```shell
mvn test
```

Una vez hayan finalizado las pruebas, puede cerrar la segunda ventana y detener el servidor en la primera ventana oprimiendo ```Ctrl``` + ```C``` en _Windows_ o _Linux_, o ```Command``` + ```.``` en _Mac_.


## ACERCA DEL PROYECTO

### Construido en

* [Maven](https://maven.apache.org/) - Dependency Management

### Control de versiones 

[Github](https://github.com/) para el versionamiento.

### Authors

[Brayan Macías](https://github.com/brayandres) 

_Fecha : 30 de agosto del 2022_ 

### License

This project is licensed under the GNU General Public License - see the [LICENSE](LICENSE)