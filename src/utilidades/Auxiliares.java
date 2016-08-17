package utilidades;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.HttpHeaders;

import utilidades.Enumeracion.Operaciones;
import capaDatos.DAOFactory;
import capaDatos.RegistroDAO;
import capaDatos.RolDAO;
import capaDatos.UsuarioDAO;
import capaNegocio.Registro;
import capaNegocio.Rol;
import capaNegocio.Usuario;

public class Auxiliares {

	public static Logger logger = Logging.obtenerServerLogger();

	public static void agregarRegistro(Usuario usuarioActual, String descripcion) {
		DAOFactory daoFactory = DAOFactory.obtenerInstancia();
		Registro registro = new Registro(descripcion,
				usuarioActual.getIdUsuario(), null);
		try {
			RegistroDAO registroDAO = (RegistroDAO) daoFactory
					.obtenerDAO(registro.getClass().getSimpleName());
			int idRegistro = registroDAO.agregar(registro);
			logger.info("Nuevo resgistro agregado " + idRegistro + " : "
					+ descripcion);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error al agregar registro", e);
		}
	}

	public static Usuario autenticar(HttpHeaders encabezados) {

		String nombreUsuario = encabezados.getRequestHeader("nombreUsuario")
				.get(0);
		String clave = encabezados.getRequestHeader("clave").get(0);
		logger.info("nombreUsuario=" + nombreUsuario);
		logger.info("clave=" + clave);
		DAOFactory daoFactory = DAOFactory.obtenerInstancia();
		Usuario usuario = null;
		try {
			UsuarioDAO usuarioDAO = (UsuarioDAO) daoFactory
					.obtenerDAO(Usuario.class.getSimpleName());
			usuario = usuarioDAO.buscar(nombreUsuario, clave);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error autenticando Usuario", e);
		}
		return usuario;
	}

	public static Set<Operaciones> getOperations(Usuario usuario) {
		Set<Operaciones> operaciones = null;
		int idRol = usuario.getIdRol();
		DAOFactory daoFactory = DAOFactory.obtenerInstancia();
		try {
			RolDAO rolDao = (RolDAO) daoFactory.obtenerDAO(Rol.class
					.getSimpleName());

			operaciones = rolDao.mostrar(idRol).getOperaciones();

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error obteniendo operaciones", e);
		}
		return operaciones;
	}

}
