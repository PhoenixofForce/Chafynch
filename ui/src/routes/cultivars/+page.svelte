<script lang="ts">
	const { data } = $props();
</script>

<div>
	<div>Cultivars</div>
	<div>
		<ul>
			{#each data.cultivars as cultivar (cultivar.id)}
				<li>
					{cultivar.name}
				</li>
			{/each}
		</ul>
	</div>
</div>
