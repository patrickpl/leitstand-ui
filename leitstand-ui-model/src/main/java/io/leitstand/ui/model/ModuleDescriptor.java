/*
 * (c) RtBrick, Inc All rights reserved, 2015 2019
 */
package io.leitstand.ui.model;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.commons.model.BuilderUtil.requires;
import static io.leitstand.commons.model.ObjectUtil.asSet;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Collections.emptySortedSet;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * The <code>ModuleDescriptor</code> describes a UI module.
 * <p>
 * Every module requires a unique name and a non-empty list of menus.
 * All other properties are optional.
 * </p>
 * <p>
 * The {@link ModuleDescriptorService} loads the <code>ModuleDescriptor</code> 
 * from the <code>module.yaml</code> file in the module directory.
 * </p>
 */
public class ModuleDescriptor {
	
	/**
	 * Returns a new builder to create an immutable <code>ModuleDescriptor</code> instance.
	 * @return a new <code>ModuleDescriptor</code> builder
	 */
	public static Builder newModuleDescriptor() {
		return new Builder();
	}
	
	/**
	 * The builder for an immutable <code>ModuleDescriptor</code> instance.
	 */
	public static class Builder {
		
		private ModuleDescriptor descriptor = new ModuleDescriptor();
		
		/**
		 * Sets the module name.
		 * @param name the module name
		 * @return a reference to this builder to continue with object creation.
		 */
		public Builder withName(String name) {
			assertNotInvalidated(getClass(), descriptor);
			descriptor.module = name;
			return this;
		}
		
		/**
		 * Sets the paths of all submodules that have to be loaded as part of this module.
		 * @param applications the submodules to be included.
		 * @return a reference to this builder to continue with object creation.
		 */
		public Builder withApplications(ModuleApplication.Builder... applications) {
			return withApplications(stream(applications)
									.map(ModuleApplication.Builder::build)
									.collect(toList()));
		}

		/**
		 * Sets the paths of all submodules that have to be loaded as part of this module.
		 * @param applications the submodules to be included.
		 * @return a reference to this builder to continue with object creation.
		 */
		public Builder withApplications(Collection<ModuleApplication> applications) {
			assertNotInvalidated(getClass(), descriptor);
			descriptor.applications = new TreeSet<ModuleApplication>((a,b) -> a.getApplication().compareTo(b.getApplication()));
			descriptor.applications.addAll(applications);
			return this;
		}
		
		/**
		 * Sets the module navigation
		 * @param builder the builders for all module menus
		 * @return a reference to this builder to continue with object creation.
		 */
		
		public Builder withNavigation(ModuleMenu.Builder...builder) {
			return withNavigation(stream(builder)
								 .map(ModuleMenu.Builder::build)
								 .collect(toList()));
		}
		
		
		/**
		 * Sets the module navigation
		 * @param menus all menus of this module
		 * @return a reference to this builder to continue with object creation.
		 */
		public Builder withNavigation(ModuleMenu...menus) {
			return withNavigation(asList(menus));
		}
		
		/**
		 * Sets the roles that have access to this module.
		 * @param roles the roles allowed to access this module
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withRoles(String... roles) {
			return withRoles(asSet(roles));
		}
		
		
		/**
		 * Sets the roles that have access to this module.
		 * @param roles the roles allowed to access this module
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withRoles(Set<String> roles) {
			descriptor.rolesAllowed = unmodifiableSet(new TreeSet<>(roles));
			return this;
		}
		
		/**
 		 * Sets the module navigation
		 * @param menus all menus of this module
		 * @return a reference to this builder to continue with object creation.
		 */
		public Builder withNavigation(List<ModuleMenu> menus) {
			assertNotInvalidated(getClass(), descriptor);
			descriptor.navigation = new ArrayList<>(menus);
			return this;
		}
		
		/**
		 * Returns the module descriptor and invalidates itself. 
		 * Any further interaction with this builder raises an exception.
		 * @return the immutable <code>ModuleDescriptor</code> instance.
		 */
		public ModuleDescriptor build() {
			try {
				assertNotInvalidated(getClass(), descriptor);
				requires(getClass(), "name", descriptor.module);
				requires(getClass(), "navigation", descriptor.navigation);
				return descriptor;
			} finally {
				descriptor = null;
			}
		}
		
	}
	
	

	private String module;
	private Set<ModuleApplication> applications = emptySet();
	private List<ModuleMenu> navigation = emptyList();
	private Set<String> rolesAllowed;

	/**
	 * Returns the module name.
	 * The module name is unique for all existing modules.
 	 * @return the name of the module.
	 */
	public String getModule() {
		return module;
	}
	
	/**
	 * Returns the set of applications to be included for this module.
	 * @return the list of applications to be included for this module.
	 */
	public Set<ModuleApplication> getApplications() {
		return unmodifiableSet(applications);
	}
	
	
	
	/**
	 * Returns the set of roles allowed to access this module.
	 * @return the set of roles allowed to access this module.
	 */
	public Set<String> getRoles() {
		if(rolesAllowed == null) {
			return emptySortedSet();
		}
		return unmodifiableSet(rolesAllowed);
	}
	
	/**
	 * Return the navigation of this module. 
	 * The navigation consist of a set of menus, 
	 * which are enabled depending on the data currently being displayed.
	 * @return the menus of this module
	 */
	public List<ModuleMenu> getNavigation() {
		return unmodifiableList(navigation);
	}
	
}
