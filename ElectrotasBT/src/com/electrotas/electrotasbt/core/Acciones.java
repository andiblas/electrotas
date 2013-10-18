package com.electrotas.electrotasbt.core;

public interface Acciones {
	
	public void cambiarColor();
	public void toggleRele();
	public boolean[] checkState();
	public void cancelar();
	
}
