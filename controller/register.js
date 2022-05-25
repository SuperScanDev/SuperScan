const validator = require('validator');
const data = [
  {
    name: 'ikhsanagil',
    email: 'ikhsanagil@gmail.com',
    password: '12345',
  },
];

const nama = data.find((datum) => datum.nama === nama);
// cek apakah data yg dikirim ada yg sama

if (nama) {
  return 'Username telah ada!';
} else {
  const emailValid = validator.isEmail(email);
  if (!emailValid) {
    return {
      error: true,
      message: 'email tidak valid',
    };
  }

  data.push();
  res.send({
    error: false,
    message: 'User Created',
  });
}
