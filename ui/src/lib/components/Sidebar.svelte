<script lang="ts">
	import { resolve } from '$app/paths';
	import { page } from '$app/state';
	import { links } from '$lib/routes';

	const currentPath = $derived(page.url.pathname);
</script>

<ul class="menu mt-2 w-full">
	{#each links as link (link.path)}
		<li class="mb-2">
			<a
				href={resolve(link.path)}
				class="{currentPath === link.path
					? 'bg-neutral text-neutral-content'
					: 'opacity-70 transition-opacity hover:opacity-100'} is-drawer-close:tooltip is-drawer-close:tooltip-right is-drawer-close:w-fit"
				data-tip={link.text}
			>
				<link.icon size="21" />
				<span class="is-drawer-close:hidden">{link.text}</span>
			</a>
		</li>
	{/each}
</ul>
