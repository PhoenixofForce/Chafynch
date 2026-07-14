<script lang="ts">
	import Button from '$lib/components/ui/Button.svelte';
	import Input from '$lib/components/ui/Input.svelte';
	import { resolve } from '$app/paths';
</script>

<div class="prose mb-8 text-center">
	<h1>Tea Tracker</h1>
</div>

<div class="card w-full max-w-96 bg-base-300 shadow-sm">
	<div class="card-body items-center">
		<h2 class="mb-4 card-title">Register</h2>
		<form class="flex w-full flex-col gap-2">
			<div class="w-full">
				<Input
					name="username"
					autocomplete="username"
					placeholder="Username*"
					required
					hint="Username is required"
					inputClass="w-full"
				/>
			</div>

			<div class="w-full">
				<Input
					name="email"
					autocomplete="email"
					placeholder="E-Mail*"
					required
					hint="E-Mail is required"
					inputClass="w-full"
				/>
			</div>

			<div class="w-full">
				<Input
					type="password"
					name="password"
					autocomplete="new-password"
					required
					placeholder="Password*"
					hint="Password is required"
					inputClass="w-full"
				/>
			</div>

			<div class="w-full">
				<Input
					type="password"
					name="confirmedPassword"
					autocomplete="new-password"
					required
					placeholder="Repeat Password*"
					hint="Password is required"
					inputClass="w-full"
				/>
			</div>

			<div class="mt-4 w-full">
				<Button type="submit" class="w-full btn-primary" label="Register" />
			</div>
		</form>
	</div>
</div>
<div class="mt-2 text-sm">
	<a href={resolve('/login')} class="link link-neutral"> Already have an account? </a>
</div>
