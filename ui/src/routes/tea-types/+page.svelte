<script lang="ts">
	const { data } = $props();
</script>

<div>
	<div>Tea types</div>
	<div>
		<ul>
			{#each data.types as types (types.id)}
				<li>
					{types.name}
				</li>
			{/each}
		</ul>
	</div>
</div>
