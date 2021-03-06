package control;

import modelos.Empleado;
import modelos.FabricaEmpleados;
import persistencia.OrmEmpleados;
import persistencia.RepositorioEmpleados;
import java.util.List;

/**
 * Clase que se encarga del registro de los empleados y contiene las principales 
 * funciones del programa
 * @author Jhon
 * @author Andres
 * @author Lucas
 * @version 1.0
 */

public class Compania {
	
	private RepositorioEmpleados repositorioEmpleados; 
	
	public Compania() {
		repositorioEmpleados = new OrmEmpleados();
	}
	
	/**
	 * Calcula la nomina semanal de todos lo empleados,
	 * para poder determinar lo que deber� pagar la compa�ia
	 * @return	nomina semanal de los empleados
	 */
	public double calcularNominaSemanal() throws EmpleadoException{
		List<Empleado> empleados = repositorioEmpleados.consultarEmpleados();
		System.out.println(empleados);
		double nominaSemanal = 0;
		
		if(empleados.isEmpty()) {
			throw new EmpleadoException("No existen empleados");
		}else {
			for (Empleado empleado : empleados) {
				nominaSemanal += empleado.calcularSalario();
			}
		}
		
		return nominaSemanal;
	}
	
	/**
	 * Adiciona un nuevo Empleado a partir de los parametros recibidos
	 * @param identificador valor de la identificacion del empleado
	 * @param nombre valor del nombre del empleado
	 * @param tipo valor de que tipo es el empleado: 'a' para Asalariado, 'h' para Horas, 'c' para Comision
	 * @param salarioSemanal valor del salario semanal del empleado
	 * @param valorHora valor por hora trabajada del empleado
	 * @param horasTrabajadas cantidad de horas trabajadas del empleado
	 * @param salarioBase valor del salario base semanal del empleado
	 * @param valorVentasRealizadas valor de las ventas semanales realizadas del empleado
	 * @return un valor booleano: 'True' si el Empleado (Asalariado, Horas o Comision) se ha creado
	 * 			o 'false' si no se especifica un tipo valido de empleado
	 * @throws EmpleadoException cuando alguna de las reglas del negocio no se cumple.
	 */
	public boolean adicionarEmpleado(String identificador, String nombre, char tipo, double salarioSemanal, 
			double valorHora, int horasTrabajadas, double salarioBase, double valorVentasRealizadas) 
	 throws EmpleadoException {
		Empleado empleadoBuscado = buscarEmpleado(identificador);
		if (empleadoBuscado == null) {
			Empleado empleadoNuevo = FabricaEmpleados.crearEmpleado(identificador, nombre, tipo, salarioSemanal, valorHora, horasTrabajadas, salarioBase, valorVentasRealizadas);
			return repositorioEmpleados.adicionarEmpleado(empleadoNuevo);
		}
		return false;
	}

	/**
	 * Busca un empleado entre los que est�n registrados, por su n�mero de identificaci�n
	 * @return el objeto empleado con la identificaci�n dada, o null si no se encuentra
	 */
	public Empleado buscarEmpleado(String identificador) {
		return repositorioEmpleados.buscarEmpleado(identificador);
	}
	
	/**
	 * Borra un empleado entre los que est�n registrados,
	 * busc�ndolo previamente por su n�mero de identificaci�n
	 * @throws EmpleadoException si no encuentra un empleado con la identificaci�n.
	 */
	public void borrarEmpleado(String identificador) throws EmpleadoException {
		Empleado empleadoBuscado = this.buscarEmpleado(identificador);
		if (empleadoBuscado == null) {
			throw new EmpleadoException("No se encuentra registrado un empleado con ese identificador");
		}
		
		boolean pudoBorrar = repositorioEmpleados.borrarEmpleado(empleadoBuscado);
		
		if (!pudoBorrar) {
			throw new EmpleadoException("No se pudo borrar al empleado. Error al llamar borrarEmpleados en la BD");
		}
	}
	
}
