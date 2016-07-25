
FactoryGirl.define do

  factory :solicitud_trabajo do
    fecha_solicitud Date.new(2014,10,10)
    aceptado false
    id_trabajo 1
    id_usuario 1
  end

  factory :solicitud_trabajo1, class: Solicitudtrabajo do
  	id 1
    fecha_solicitud Date.new(2014,10,10)
    aceptado false
    id_trabajo 1
    id_usuario 1
  end

  factory :solicitud_trabajo2, class: Solicitudtrabajo do
  	id 2
    fecha_solicitud Date.new(2014,10,10)
    aceptado true
    id_trabajo 1
    id_usuario 3
  end

  factory :solicitud_trabajo3, class: Solicitudtrabajo do
  	id 3
    fecha_solicitud Date.new(2014,10,10)
    aceptado false
    id_trabajo 2
    id_usuario 2
  end

end