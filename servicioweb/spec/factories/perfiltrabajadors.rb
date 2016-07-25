FactoryGirl.define do
	
  factory :perfiltrabajador do
	id 1
    puntaje_promedio 0
    id_usuario 1
    id_tipo_trabajo 1
    habilitado true
  end

  factory :perfiltrabajador1, class: Perfiltrabajador do
  	id 2
    puntaje_promedio 0
    id_usuario 2
    id_tipo_trabajo 2
    habilitado true
  end

  factory :perfiltrabajador2, class: Perfiltrabajador do
    id 3
    puntaje_promedio 0
    id_usuario 2
    id_tipo_trabajo 1
    habilitado true
  end

  factory :perfiltrabajador3, class: Perfiltrabajador do
    id 4
    puntaje_promedio 0
    id_usuario 3
    id_tipo_trabajo 1
    habilitado true
  end

  factory :perfiltrabajador4, class: Perfiltrabajador do
    id 5
    puntaje_promedio 0
    id_usuario 1
    id_tipo_trabajo 2
    habilitado true
  end

  factory :perfiltrabajador5, class: Perfiltrabajador do
    id 6
    puntaje_promedio 0
    id_usuario 4
    id_tipo_trabajo 1
    habilitado true
  end

  factory :perfiltrabajador6, class: Perfiltrabajador do
    id 7
    puntaje_promedio 0
    id_usuario 5
    id_tipo_trabajo 1
    habilitado false
  end

end
