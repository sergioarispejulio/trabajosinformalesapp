FactoryGirl.define do
  factory :comentarioatrabajador do

  	comentario "prueba"
    id_usuario_que_comenta 2
    id_trabajador_que_recibe_comentario 1
    id_perfil_trabajo 1
    
  end

  factory :comentarioatrabajador1, class: Comentarioatrabajador do

  	comentario "prueba"
    id_usuario_que_comenta 3
    id_trabajador_que_recibe_comentario 1
    id_perfil_trabajo 1
    
  end

end
