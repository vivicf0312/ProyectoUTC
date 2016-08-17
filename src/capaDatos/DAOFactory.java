package capaDatos;

import java.util.HashMap;

@SuppressWarnings("rawtypes")
public class DAOFactory {

	HashMap<String, AbstractBaseDAO> classMap = new HashMap<String, AbstractBaseDAO>();
	private static DAOFactory daoFactory = null;

	private DAOFactory() {
		classMap.put("Usuario", new UsuarioDAO());
		classMap.put("Articulo", new ArticuloDAO());
		classMap.put("Rol", new RolDAO());
		classMap.put("Ubicacion", new UbicacionDAO());
		classMap.put("Registro", new RegistroDAO());
	}

	public synchronized static DAOFactory obtenerInstancia() {

		if (daoFactory == null) {
			daoFactory = new DAOFactory();
		}
		return daoFactory;
	}

	public AbstractBaseDAO obtenerDAO(String className) throws Exception {

		if (classMap.containsKey(className)) {
			return classMap.get(className);

		}
		throw new Exception("Nombre de clase invalido");
	}
}
