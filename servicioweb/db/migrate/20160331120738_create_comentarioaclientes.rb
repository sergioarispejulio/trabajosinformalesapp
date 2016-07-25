class CreateComentarioaclientes < ActiveRecord::Migration
  def change
    create_table :comentarioaclientes do |t|
      t.string :comentario
      t.string :nombre_usuario_que_comenta
      t.integer :id_trabajador_que_comenta
      t.integer :id_usuario_que_recibe_comentario
      
      t.timestamps null: false
    end
  end
end
