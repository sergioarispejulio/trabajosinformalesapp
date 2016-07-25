
FactoryGirl.define do

  factory :trabajo do
  	id 6
    titulo "trabajo"
	descripcion "trabajo"
	coordenadax 15
	coordenaday 15
	fecha_de_requerimiento Date.new(2014,10,10)
	id_tipo_trabajo 1
	id_solicitante 1
	ciudad "La Paz"
	activo true
  end

  factory :trabajo1, class: Trabajo do
  	id 1
    titulo "trabajo1"
	descripcion "trabajo1"
	coordenadax 15
	coordenaday 15
	fecha_de_requerimiento Date.new(2014,10,10)
	id_tipo_trabajo 1
	id_solicitante 1
	ciudad "Cochabamba"
	activo true
  end

  factory :trabajo2, class: Trabajo do
  	id 2
    titulo "trabajo2"
	descripcion "trabajo2"
	coordenadax 15
	coordenaday 15
	fecha_de_requerimiento Date.new(2014,10,10)
	id_tipo_trabajo 1
	id_solicitante 1
	ciudad "Cochabamba"
	activo true
  end

  factory :trabajo3, class: Trabajo do
  	id 3
    titulo "trabajo3"
	descripcion "trabajo3"
	coordenadax 15
	coordenaday 15
	fecha_de_requerimiento Date.new(2014,10,10)
	id_tipo_trabajo 1
	id_solicitante 1
	ciudad "Cochabamba"
	activo true
  end

  factory :trabajo4, class: Trabajo do
  	id 4
    titulo "trabajo4"
	descripcion "trabajo4"
	coordenadax 15
	coordenaday 15
	fecha_de_requerimiento Date.new(2014,10,10)
	id_tipo_trabajo 1
	id_solicitante 1
	ciudad "Cochabamba"
	activo false
  end

  factory :trabajo5, class: Trabajo do
  	id 5
    titulo "trabajo5"
	descripcion "trabajo5"
	coordenadax 15
	coordenaday 15
	fecha_de_requerimiento Date.new(2014,10,10)
	id_tipo_trabajo 2
	id_solicitante 1
	ciudad "Cochabamba"
	activo false
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