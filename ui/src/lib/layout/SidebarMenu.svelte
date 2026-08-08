<script lang="ts">
	import { resolve } from '$app/paths';
	import { page } from '$app/state';
	import { sidebarLinks } from '$lib/layout/routes';

	const currentPath = $derived(page.url.pathname);
</script>

<ul class="menu mt-2 w-full">
	{#each sidebarLinks as link (link.path)}
		<li class="mb-2">
			<a
				class="{currentPath === link.path
					? 'bg-neutral text-neutral-content'
					: 'opacity-70 transition-opacity hover:opacity-100'} is-drawer-close:tooltip is-drawer-close:tooltip-right is-drawer-close:w-fit"
				data-tip={link.label}
				href={resolve(link.path ?? '/')}
			>
				<link.icon size="21" />
				<span class="is-drawer-close:hidden">{link.label}</span>
			</a>
		</li>
	{/each}
</ul>
