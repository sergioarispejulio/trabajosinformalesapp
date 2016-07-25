FactoryGirl.define do

  factory :user do
    id 1
    nickname "prueba"
    telefono 123456789
    password "123"
    email "prueba@gmail.com"
    nombre "Nombre"
    apellido "Apellido"
    ciudad "Cochabamba"
    ci "123456"
    fecha_nacimiento Date.new(2014,2,3)
    salt "prueba"
    crypted_password { Sorcery::CryptoProviders::BCrypt.encrypt("123", "prueba") }
  end

  factory :user1, class: User do 
    id 2
    nickname "prueba1"
    telefono 123456788
    password "123"
    email "prueba@gmail.com"
    nombre "Nombre"
    apellido "Apellido"
    ciudad "Cochabamba"
    ci "123456"
    fecha_nacimiento Date.new(2014,2,3)
    salt "prueba"
    crypted_password { Sorcery::CryptoProviders::BCrypt.encrypt("123", "prueba") }
  end

  factory :user2, class: User do 
    id 3
    nickname "prueba2"
    telefono 123456787
    password "123"
    email "prueba@gmail.com"
    nombre "Nombre"
    apellido "Apellido"
    ciudad "Cochabamba"
    ci "123456"
    fecha_nacimiento Date.new(2014,2,3)
    salt "prueba"
    crypted_password { Sorcery::CryptoProviders::BCrypt.encrypt("123", "prueba") }
  end

  factory :user3, class: User do 
    id 4
    nickname "prueba3"
    telefono 123456786
    password "123"
    email "prueba@gmail.com"
    nombre "Nombre"
    apellido "Apellido"
    ciudad "Cochabamba"
    ci "123456"
    fecha_nacimiento Date.new(2014,2,3)
    salt "prueba"
    crypted_password { Sorcery::CryptoProviders::BCrypt.encrypt("123", "prueba") }
  end

  factory :user4, class: User do 
    id 5
    nickname "prueba4"
    telefono 123456785
    password "123"
    email "prueba@gmail.com"
    nombre "Nombre"
    apellido "Apellido"
    ciudad "Cochabamba"
    ci "123456"
    fecha_nacimiento Date.new(2014,2,3)
    salt "prueba"
    crypted_password { Sorcery::CryptoProviders::BCrypt.encrypt("123", "prueba") }
  end

  factory :user5, class: User do 
    id 6
    nickname "prueba5"
    telefono 123456784
    password "123"
    email "prueba@gmail.com"
    nombre "Nombre"
    apellido "Apellido"
    ciudad "Cochabamba"
    ci "123456"
    fecha_nacimiento Date.new(2014,2,3)
    salt "prueba"
    crypted_password { Sorcery::CryptoProviders::BCrypt.encrypt("123", "prueba") }
  end

  #FactoryGirl.define do
    #factory :grupo_publico, class: Grupo do
    #nombre "Publico" 
    #descripcion "todos deben tener ingreso a temas publicos"
    ##estado false
    #llave "publico"
    #end
  #end
end