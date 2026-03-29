<script>
	import { resolve } from '$app/paths';
	import { PanelLeftClose, PanelLeftOpen } from '@lucide/svelte';

	let { drawerOpen, breadcrumbs } = $props();
</script>

<nav class="navbar flex w-full justify-between bg-base-300 shadow-sm">
	<div class="flex items-center">
		<label
			for="sidebar"
			aria-label="open sidebar"
			class="btn swap btn-square opacity-70 btn-ghost transition-all hover:scale-110 hover:opacity-100"
		>
			<!-- Sidebar toggle icon -->
			<input type="checkbox" bind:checked={drawerOpen} />
			<div class="swap-on"><PanelLeftClose class="is-drawer-close:hidden" /></div>
			<div class="swap-off"><PanelLeftOpen class="is-drawer-close:hidden" /></div>
		</label>
		<div class="breadcrumbs ml-2 text-sm">
			<ul>
				{#each breadcrumbs as breadcrumb, i (i)}
					<li>
						<span class="inline-flex items-center gap-2">
							<breadcrumb.icon />
							<a href={resolve(breadcrumb.path)}>{breadcrumb.label}</a>
						</span>
					</li>
				{/each}
			</ul>
		</div>
	</div>
	<div class="dropdown dropdown-end">
		<div tabindex="0" class="mr-2 h-10 w-10 rounded-full bg-primary"></div>
		<ul tabindex="-1" class="dropdown-content menu z-1 w-52 rounded-box bg-base-100 p-2 shadow-sm">
			<li><a>Item 1</a></li>
			<li><a>Item 2</a></li>
		</ul>
	</div>
</nav>
