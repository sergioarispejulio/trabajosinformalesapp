class CreateTipotrabajos < ActiveRecord::Migration
  def change
    create_table :tipotrabajos do |t|
      t.string :nombre
      t.string :descripcion
      t.string :url_foto
      t.timestamps null: false
    end
  end
end
