<script lang="ts">
	import type { Snippet } from 'svelte';

	interface TabDefinition {
		id: string;
		label?: string;
		content?: Snippet<[TabDefinition]>;
	}

	const uid = $props.id();
	let {
		tabs,
		active = $bindable(typeof tabs[0] === 'string' ? tabs[0] : tabs[0]?.id),
		class: className,
		tabClass,
		contentClass,
		renderTab,
		children
	}: {
		tabs: (TabDefinition | string)[];
		active?: string;
		class?: string;
		tabClass?: string;
		contentClass?: string;
		renderTab?: Snippet<[TabDefinition]>;
		children?: Snippet;
	} = $props();

	let tabItems = $derived<TabDefinition[]>(
		tabs.map((tab) => (typeof tab === 'string' ? { id: tab, label: tab } : tab))
	);

	$effect(() => {
		if (!tabItems.some((e) => e.id === active)) {
			active = tabItems[0]?.id;
		}
	});
</script>

<div class="tabs-border tabs w-full {className}" role="tablist">
	{#each tabItems as tab (tab.id)}
		<label class="tab capitalize {tabClass}">
			<input
				name="tabs-{uid}"
				class="checked:text-primary"
				aria-label={tab.label ?? tab.id}
				checked={active === tab.id}
				onchange={() => (active = tab.id)}
				type="radio"
			/>
			{tab.label ?? tab.id}
		</label>
		<div class="tab-content px-6 py-4 {contentClass}">
			{@render (tab.content ?? renderTab)?.(tab)}
		</div>
	{/each}

	{@render children?.()}
</div>
