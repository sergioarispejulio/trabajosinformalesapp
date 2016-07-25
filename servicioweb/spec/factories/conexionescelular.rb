

FactoryGirl.define do

  factory :conexionescelular do
    token "acY,%aYT8acY,%aYT8acY,%aYT8acY,%aYT8acY,%aYT8acY,%"
    modelo_celular "aaa"
    sistema_operativo "bbb"
    fecha_login Date.new(2014,10,10)
    fecha_logout nil
    id_usuario 1
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