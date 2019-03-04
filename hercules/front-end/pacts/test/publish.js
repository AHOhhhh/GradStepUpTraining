const pact = require('@pact-foundation/pact-node')
const path = require('path')

const opts = {
  pactUrls: [path.resolve(__dirname, '../contracts/project_api_consumer-project_api_provider.json')],
  pactBroker: 'http://pact.hlp.fun',
  tags: ['test'],
  consumerVersion: '1.0.0'
}

pact.publishPacts(opts)
  .then(() => {
    console.log('Pact contract publishing complete!')
    console.log('')
    console.log('Head over to http://pact.hlp.fun')
    console.log('to see your published contracts.')
  })
  .catch(e => {
    console.log('Pact contract publishing failed: ', e)
  })
