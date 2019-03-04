const path = require('path')
const test = require('ava')
const Pact = require('pact')

const axios = require('axios')


const url = 'http://localhost'
const port = 5000

const getProjects = () => {
  return axios.request({
    method: 'GET',
    baseURL: `${url}:${port}`,
    url: '/projects',
    headers: { Accept: 'application/json' }
  })
}

const getProjectById = (id) => {
  return axios.request({
    method: 'GET',
    baseURL: `${url}:${port}`,
    url: `/projects/${id}`,
    headers: { Accept: 'application/json' }
  })
}

const provider = Pact({
  port,
  log: path.resolve(process.cwd(), 'logs', 'mockserver-integration.log'),
  dir: path.resolve(process.cwd(), 'contracts'),
  spec: 2,
  consumer: 'Project API Consumer',
  provider: 'Project API Provider'
})

const EXPECTED_BODY = [{
  id: '1',
  name: 'HNA Logistics Platform',
  members: [
    {
      id: 'jtqiu',
      name: 'Juntao Qiu'
    },
    {
      id: 'qding',
      name: 'Qian Ding'
    }
  ]
}, {
  id: '2',
  name: 'Huawei CBG',
  members: [
    {
      id: 'xchou',
      name: 'Xiaocheng Hou'
    }
  ]
}
]

test.before('Setting up API expectations', async () => {
  await provider.setup()

  const interaction = {
    state: 'I have a list of projects',
    uponReceiving: 'A request for projects',
    withRequest: {
      method: 'GET',
      path: '/projects',
      headers: {Accept: 'application/json'}
    },
    willRespondWith: {
      status: 200,
      headers: {'Content-Type': 'application/json'},
      body: EXPECTED_BODY
    }
  }

  await provider.addInteraction(interaction)

  const yaInteraction = {
    state: 'A specific project',
    uponReceiving: 'A request for a project',
    withRequest: {
      method: 'GET',
      path: '/projects/1',
      headers: {Accept: 'application/json'}
    },
    willRespondWith: {
      status: 201
    }
  }

  await provider.addInteraction(yaInteraction)
})

test('Get projects', async t => {
  const response = await getProjects()
  t.deepEqual(response.data, EXPECTED_BODY)
})

test('Get a project', async t => {
  const response = await getProjectById(1)
  t.deepEqual(response.status, 201)
})

test.always.after('pact.js mock server graceful shutdown', async () => {
  await provider.finalize()
})