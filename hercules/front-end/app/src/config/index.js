const DEVELOPMENT_API_URL = '/webapi';
const PRODUCTION_API_URL = '/webapi';
const environment = process.env.NODE_ENV || 'development';

const URL_MAP = {
  development: DEVELOPMENT_API_URL,
  production: PRODUCTION_API_URL,
};

const BASE_URL = URL_MAP[environment];

export default BASE_URL;
