class CreatePerfiltrabajadors < ActiveRecord::Migration
  def change
    create_table :perfiltrabajadors do |t|
      t.integer :puntaje_promedio
      t.integer :id_usuario
      t.integer :id_tipo_trabajo
      t.boolean :habilitado
      t.timestamps null: false
    end
  end
end
