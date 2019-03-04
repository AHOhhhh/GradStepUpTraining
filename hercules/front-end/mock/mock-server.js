const swaggerParser = require('swagger-parser')
const jsf = require('json-schema-faker')
const express = require('express')

const {each} = require('lodash')

const app = express()

const initializeMethods = (methods, path) => {
  const expressPath = path.replace('{', ':').replace('}', '');
  each(methods, (body, method) => {
    const schema = body.responses[200]
    app[method](expressPath, (req, res) => {
      jsf.resolve(schema).then((sample) => {
        res.status(200).send(sample.schema)
      })
    })
  })
}

const initializeEndpoints = (api) => {
  each(api.paths, (methods, path) => {
    initializeMethods(methods, (api.basePath || '') + path)
  })
}

const schemaDefinition = './schema/webapi.swagger.yaml';
const port = 8080;
swaggerParser.dereference(schemaDefinition).then(api => {
  initializeEndpoints(api)
  app.listen(port, '0.0.0.0')
  console.log(`mock server is running on port ${port}, schema definition is under ${schemaDefinition}`)
});