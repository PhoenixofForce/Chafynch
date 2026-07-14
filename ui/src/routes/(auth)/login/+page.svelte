<script lang="ts">
	import { resolve } from '$app/paths';
	import Button from '$lib/components/ui/Button.svelte';
	import Input from '$lib/components/ui/Input.svelte';
</script>

<div class="prose mb-8 text-center">
	<h1>Tea Tracker</h1>
</div>

<div class="card w-full max-w-96 bg-base-300 shadow-sm">
	<div class="card-body items-center">
		<h2 class="mb-4 card-title">Login</h2>
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
					type="password"
					name="password"
					autocomplete="current-password"
					required
					placeholder="Password*"
					hint="Password is required"
					inputClass="w-full"
				/>
			</div>

			<div class="flex w-full flex-col justify-between gap-2 sm:flex-row sm:items-center sm:gap-0">
				<div>
					<label class="label">
						<input type="checkbox" checked={false} class="checkbox" />
						Stay logged in
					</label>
				</div>
				<div>
					<a class="link link-primary"> Forgot Password? </a>
				</div>
			</div>

			<div class="mt-4 w-full">
				<Button type="submit" class="w-full btn-primary" label="Login" />
			</div>
		</form>

		<div class="divider">OR</div>

		<div class="w-full">
			<Button class="w-full btn-sm" label="Login with Authentik" />
		</div>
	</div>
</div>
<div class="mt-2 text-sm">
	<a href={resolve('/register')} class="link link-neutral"> Create new user </a>
</div>
