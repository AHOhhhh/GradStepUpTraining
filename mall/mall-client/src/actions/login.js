import request from 'superagent';

export const login = (name, password) => {
  request.post(`/login`)
    .set('Content-Type', 'application/json')
    .send({ name, password }).end((err, res) => {
      console.log('res :', res);
    });
}


