# ENSportive frontend

This is an Angular 17 application project.

## Prerequisites

Before installing and running this project, ensure you have the following prerequisites:

1. **Node.js**: Angular 17 requires a compatible version of Node.js. You can download and install Node.js from [here](https://nodejs.org/).

2. **Angular CLI**: Install the Angular CLI globally using npm:

    ```bash
    npm i @angular/cli@17 -g
    ```

3. **Recommended Node Version**: To ensure compatibility, it's recommended to use version v20.11.1 of Node.js. You can use tools like [Node Version Manager (nvm)](https://github.com/nvm-sh/nvm) to manage multiple Node.js versions.

## Installation

1. Clone the repository:

    ```bash
    git clone https://tools.ages.pucrs.br/ensportive/ensportive-frontend.git
    ```

2. Navigate to the project directory:

    ```bash
    cd ensportive-frontend
    ```

3. Install dependencies:

    ```bash
    npm install
    ```

## Usage

1. To start the development server, run:

    ```bash
    ng serve --open
    ```

   This will run the application in development mode. Open [http://localhost:4200](http://localhost:4200) in your browser to view it.

2. To run unit tests, execute:

    ```bash
    ng test
    ```
3. To start as a docker container, run:

    ```bash
   docker build -t angular-docker .
   docker run -p 4201:4200 angular-docker
    ```

   This will run the application in development mode. Open [http://localhost:4200](http://localhost:4200) in your browser to view it.

4. To determine the enviroment

    You must go to src/app/enviroment/enviroment.ts and let uncommented the enviroment in which you wanna use the application
