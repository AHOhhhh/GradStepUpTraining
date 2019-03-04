const jsf = require('json-schema-faker');
const glob = require('glob')

glob('./schema/*.schema.json', (er, files) => {
  files.forEach(file => {
    let schema = require(file);
    jsf.resolve(schema).then((sample) => {
      console.log(JSON.stringify(sample, null, 4))
    })
  })
})