class CreateComentarioatrabajadors < ActiveRecord::Migration
  def change
    create_table :comentarioatrabajadors do |t|
      t.string :comentario
      t.string :nombre_usuario_que_comenta
      t.integer :id_usuario_que_comenta
      t.integer :id_trabajador_que_recibe_comentario
      t.integer :id_perfil_trabajo
      
      t.timestamps null: false
    end
  end
end
