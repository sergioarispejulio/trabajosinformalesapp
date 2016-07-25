class SorceryCore < ActiveRecord::Migration
  def change
    create_table :users do |t|
      t.string :nickname,            :null => false
      t.integer :telefono,            :null => false
      t.string :email
      t.string :crypted_password
      t.string :salt
      t.string :nombre
      t.string :apellido
      t.string :ciudad
      t.string :ci
      t.string :url_foto
      t.date :fecha_nacimiento
      t.timestamps
    end

    add_index :users, :nickname, unique: true
    add_index :users, :telefono, unique: true
  end
end